package com.eros.shell.jline;

import java.util.List;
import java.util.logging.Logger;

import com.eros.shell.command.BaseCommand;
import com.eros.shell.config.ErosShellConfig;
import com.eros.shell.util.ShellLoggerUtil;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

/**
 * Completer For Eros System.
 *
 * @author Eros
 * @since   2020-01-03 10:58
 */
public class ErosCompleter implements Completer {

    private static final Logger logger = ShellLoggerUtil.getLogger(Completer.class);

    @Override
    public void complete(LineReader lineReader, ParsedLine parsedLine, List<Candidate> list) {
        if(ErosShellConfig.isEnableTestLog())
            logger.info(String.format("LineReader-Buffer:%s,ParsedLine-Words:%s,Candidates:%s", lineReader.getBuffer(), parsedLine.words(), list));

        // Guarantee that the final token is the one we're expanding
        String buffer = lineReader.getBuffer().substring(0, parsedLine.cursor());
        String token = "";
        if (!buffer.endsWith(" ")) {
            String[] tokens = buffer.split(" ");
            if (tokens.length != 0) {
                token = tokens[tokens.length - 1];
            }
        }
        completeCommand(buffer, token, list);
    }

    private int completeCommand(String buffer, String token, List<Candidate> candidates) {
        for (BaseCommand cmd : BaseCommand.getCommands()) {
            if (cmd.getCmdStr().startsWith(token)) {
                candidates.add(new Candidate(cmd.getCmdStr()));
            }
        }
        return buffer.lastIndexOf(" ") + 1;
    }
}
