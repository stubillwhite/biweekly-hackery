import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.impl.PosixSysTerminal;
import org.jline.utils.NonBlockingReader;

import java.io.IOException;
import java.nio.charset.Charset;

// https://stackoverflow.com/questions/1066318/how-to-read-a-single-char-from-the-console-in-java-as-the-user-types-it

public class ScratchpadTwo {

    public static void main(String[] args) throws Exception {
//        AnsiConsole.systemInstall();

        String prompt = Ansi.ansi()
                .eraseScreen()
                .fg(Ansi.Color.BLUE).bold().a("Console")
                .fgBright(Ansi.Color.BLACK).bold().a(" > ")
                .reset().toString();

        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .dumb(false)
                .encoding(Charset.forName("UTF-8"))
                .name("Terminal")
                .jna(false)
                .jansi(true)
                .build();

//        Terminal terminal = TerminalBuilder.builder().system(true).build();

//        LineReader reader = LineReaderBuilder.builder()
//                .terminal(terminal);

//        final Terminal terminal = LineReaderBuilder.builder().build().getTerminal();

//        Terminal terminal = TerminalBuilder.builder()
//                .jansi(true)
//                .system(true)
//                .build();

// raw mode means we get keypresses rather than line buffered input
        terminal.enterRawMode();
//        NonBlockingReader reader = terminal.reader();
//        int read = reader.read();
//        System.out.println(read);


//        terminal.enterRawMode();
//
        final NonBlockingReader reader = terminal.reader();

        while (true) {
            var c = reader.read();
            if (c != -1) {
                System.out.println(c);
//                terminal.writer().println(c);
                
                if (c == 23) break;
            }
        }

        reader.close();
        terminal.close();
    }
}
