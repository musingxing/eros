package com.eros.shell.main;

import com.eros.common.util.XMLParser;
import com.eros.shell.command.BaseCommand;
import com.eros.shell.command.impl.HistoryCommand;
import com.eros.shell.exception.CommandException;
import com.eros.shell.jline.ErosCompleter;
import com.eros.shell.util.ShellLoggerUtil;
import com.eros.shell.xml.CommandXML;
import com.eros.shell.xml.CommandsXML;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.DefaultHighlighter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The command line client for eros.
 */
public class ErosMain {

    private static final Logger LOG = ShellLoggerUtil.getLogger(ErosMain.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static void usage() {
        System.err.println("Eros cmd args:");
        List<BaseCommand> commands = BaseCommand.getCommands();
        Collections.sort(commands, new Comparator<BaseCommand>() {
            @Override
            public int compare(BaseCommand o1, BaseCommand o2) {
                return o1.getCmdStr().compareTo(o2.getCmdStr());
            }
        });
        for (BaseCommand command : commands) {
            System.err.println("\t" + command.getCmdStr());
        }
    }

    private final HistoryCommand history;

    public ErosMain(String[] args) {
        loadAllCommands();
        this.history = (HistoryCommand) BaseCommand.getCommand("history");
    }

    private CommandsXML getCommandsXMLDesc() throws Exception {
        String file = "commands.xml";
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
        if (in == null)
            throw new RuntimeException("Not found resource: " + file);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder content = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            LOG.info("\n" + content.toString());
            return (CommandsXML) XMLParser.parseObjectFromXML(content.toString(), CommandsXML.class);

        } finally {
            if (in != null)
                in.close();
        }
    }

    private void loadAllCommands() {
        LOG.info("Start to load all commands from commands.xml");
        try {
            CommandsXML cmdsXML = getCommandsXMLDesc();
            List<CommandXML> commandsClazz = cmdsXML.getCommands();
            if (commandsClazz == null || commandsClazz.isEmpty()) {
                LOG.log(Level.WARNING, "Not found commands from commands.xml");
                return;
            }
            for (CommandXML cmdXml : commandsClazz) {
                LOG.info("Command xml: " + cmdXml);
                try {
                    String clazz = cmdXml.getClazz();
                    if (clazz == null || clazz.isEmpty()) {
                        LOG.log(Level.WARNING, "Loading " + cmdXml + ", status=failed");
                    }
                    Class<BaseCommand> clazzQSDM = ((Class<BaseCommand>) Class.forName(clazz));
                    clazzQSDM.newInstance().addToMap();
                } catch (Throwable e) {
                    LOG.log(Level.WARNING, "Fail to add instance", e);
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException("Fail to load command instance", e);
        }
    }

    public static void main(String[] args) throws Exception {
        ErosMain main = new ErosMain(args);
        main.run();
    }

    void run() throws Exception {
        System.out.println("Welcome to Eros!");
        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .build();
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(new ErosCompleter())
                .highlighter(new DefaultHighlighter())
                .appName("eros")
                .build();
        try {
            String line;
            while ((line = reader.readLine(getPrompt())) != null) {
                try {
                    executeLine(line);
                } catch (Throwable e) {
                    LOG.log(Level.WARNING, "Execute command: '" + line + "' failure", e);
                    System.err.println("\n Error: " + e.getMessage());
                }
            }
        } catch (UserInterruptException e) {
            System.exit(1);
        } catch (EndOfFileException e) {
            System.exit(1);
        }
    }

    protected String getPrompt() {
        return "eros(" + DATE_FORMAT.format(System.currentTimeMillis()) + "):" + history.getCommandCount() + "> ";
    }

    public void executeLine(String line) throws CommandException {
        if (!line.equals("")) {
            LOG.info("Parsing '" + line + "'");
            BaseCommand command = parseCommand(line);
            history.addToHistory(line);
            LOG.info("Processing '" + command + "'");
            command.exec();
            System.out.println();
        }
    }

    public static final Pattern ARGS_PATTERN = Pattern.compile("\\s*([^\"\']\\S*|\"[^\"]*\"|'[^']*')\\s*");
    public static final Pattern QUOTED_PATTERN = Pattern.compile("^([\'\"])(.*)(\\1)$");

    /**
     * Breaks a string into command + arguments.
     *
     * @param cmdstring string of form "cmd arg1 arg2..etc"
     * @return true if parsing succeeded.
     */
    private BaseCommand parseCommand(String cmdstring) throws CommandException {
        String commandLineStr = cmdstring.trim();
        String likeCmdStr = null;
        int len = 0;
        List<BaseCommand> commands = BaseCommand.getCommands();
        for (BaseCommand command : commands) {
            String cmdStr = command.getCmdAndOpStr();
            if (!commandLineStr.startsWith(cmdStr) || cmdStr.length() <= len)
                continue;
            likeCmdStr = cmdStr;
            commandLineStr = commandLineStr.substring(cmdStr.length());
        }
        if (likeCmdStr == null) {
            usage();
            throw new CommandException("Command not found '" + commandLineStr + "'");
        }

        List<String> args = new LinkedList<String>();
        Matcher matcher = ARGS_PATTERN.matcher(cmdstring.trim());
        while (matcher.find()) {
            String value = matcher.group(1);
            if (QUOTED_PATTERN.matcher(value).matches()) {
                // Strip off the surrounding quotes
                value = value.substring(1, value.length() - 1);
            }
            args.add(value);
        }
        String[] parsedCmd = new String[args.size()];
        for (int index = 0, size = args.size(); index < size; index++) {
            parsedCmd[index] = args.get(index);
        }
        BaseCommand baseCommand = BaseCommand.getCommand(likeCmdStr);
        baseCommand.parse(parsedCmd);
        return baseCommand;
    }

}

