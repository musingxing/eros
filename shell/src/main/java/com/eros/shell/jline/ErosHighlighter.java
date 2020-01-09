package com.eros.shell.jline;

import com.eros.shell.command.BaseCommand;
import com.eros.shell.config.ErosShellConfig;
import com.eros.shell.util.ShellLoggerUtil;
import org.apache.log4j.Logger;
import org.jline.reader.Highlighter;
import org.jline.reader.LineReader;
import org.jline.utils.AttributedString;

/**
 * Highlighter For Eros System.
 *
 * @author Eros
 * @since 2020-01-03 10:58
 */
public class ErosHighlighter implements Highlighter {

    private static final Logger logger = ShellLoggerUtil.getLogger(ErosHighlighter.class);

    @Override
    public AttributedString highlight(LineReader lineReader, String s) {
        if(ErosShellConfig.isEnableTestLog())
            logger.info(String.format("LineReader-Buffer:%s,s:%s", lineReader.getBuffer(), s));

        String buffer = lineReader.getBuffer().toString();
        for (BaseCommand cmd : BaseCommand.getCommands()) {
            String command = cmd.getCmdStr();
            if (!buffer.startsWith(command))
                continue;

            if (buffer.trim().length() == command.length()
                    || buffer.charAt(command.length()) == ' ')
                return new AttributedString(command);

        }
        return new AttributedString(s);
    }
}
