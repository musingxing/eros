package com.eros.shell.main;

import com.eros.shell.command.BaseCommand;
import com.eros.shell.command.impl.HelpCommand;
import com.eros.shell.command.impl.HistoryCommand;
import com.eros.shell.command.impl.QuitCommand;
import com.eros.shell.exception.CommandException;
import com.eros.shell.jline.ErosCompleter;
import com.eros.shell.util.ShellLoggerUtil;
import org.apache.log4j.Logger;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The command line client for eros.
 */
public class ErosMain {

    private static final Logger LOG = ShellLoggerUtil.getLogger(ErosMain.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static {
        try{
            new QuitCommand();
            new HelpCommand();
            Class<BaseCommand> clazzCDM = ((Class<BaseCommand>)Class.forName("com.eros.datagen.command.CreateDataMakeCommand"));
            clazzCDM.newInstance();
            Class<BaseCommand> clazzQSDM = ((Class<BaseCommand>)Class.forName("com.eros.datagen.command.QueryStatusDataMakeCommand"));
            clazzQSDM.newInstance();
            Class<BaseCommand> clazzSDM = ((Class<BaseCommand>)Class.forName("com.eros.datagen.command.StopDataMakeCommand"));
            clazzSDM.newInstance();
        }
        catch (Throwable e){
            LOG.error("Fail to add instance", e);
        }
    }

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
        this.history = new HistoryCommand();
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
//                .highlighter(new ErosHighlighter())
                .appName("eros")
                .build();
        String line;
        while ((line = reader.readLine(getPrompt())) != null) {
            try{
                executeLine(line);
            }catch (Throwable e){
                LOG.warn("Execute command: '" + line + "' failure", e);
                System.err.println("\n Error: " + e.getMessage());
            }
        }
    }

    protected String getPrompt() {
        return "eros(" + DATE_FORMAT.format(System.currentTimeMillis()) + "):" + history.getCommandCount() + "> ";
    }

    public void executeLine(String line) throws CommandException {
        if (!line.equals("")) {
            if (LOG.isInfoEnabled())
                LOG.info("Parsing '" + line + "'");
            BaseCommand command = parseCommand(line);
            history.addToHistory(line);
            if (LOG.isInfoEnabled())
                LOG.info("Processing '" + command + "'");
            command.exec();
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
    public BaseCommand parseCommand(String cmdstring) throws CommandException {
        String commandLineStr = cmdstring.trim();
        String likeCmdStr = null;
        int len = 0;
        List<BaseCommand> commands = BaseCommand.getCommands();
        for (BaseCommand command : commands) {
            String cmdStr = command.getCmdStr();
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
        args.add(likeCmdStr);
        Matcher matcher = ARGS_PATTERN.matcher(commandLineStr);
        while (matcher.find()) {
            String value = matcher.group(1);
            if (QUOTED_PATTERN.matcher(value).matches()) {
                // Strip off the surrounding quotes
                value = value.substring(1, value.length() - 1);
            }
            args.add(value);
        }
        String[] parsedCmd = new String[args.size()];
        for(int index = 0, size = args.size();  index < size; index++){
            parsedCmd[index] = args.get(index);
        }
        BaseCommand baseCommand = BaseCommand.getCommand(likeCmdStr);
        baseCommand.parse(parsedCmd);
        return baseCommand;
    }
}

