package cache.simpledemo;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.ParsedLine;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.List;

/**
 * @author lidong@date 2024-06-21@version 1.0
 */
public class CLI {
    AllInOneDemo demo;
    public CLI(AllInOneDemo demo) throws IOException {
        this.demo = demo;
        Terminal terminal = TerminalBuilder.builder().system(true).build();
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(new StringsCompleter("get", "update", "exit", "help"))
                .build();

        String line;
        while ((line = reader.readLine("> ")) != null) {

            if(line.trim().isEmpty())
                continue;
            ParsedLine parsedLine = reader.getParsedLine();
            List<String> words = parsedLine.words();
            try {
                execCommand(words);
            }catch (Throwable t){
                t.printStackTrace();
            }
//            System.out.println("You typed: " + line);
        }
    }

    private void execCommand(List<String> words) {
        switch (words.get(0)){
            case "get":
                cmd_get(words);
                break;
            case "update":
                cmd_update(words);
                break;
            case "exit":
                System.exit(0);
        }
    }

    private void cmd_update(List<String> words) {
        int key = Integer.parseInt(words.get(1));
        demo.scenarioDataUpdated(key);
    }

    private void cmd_get(List<String> words) {
        int client = Integer.parseInt(words.get(1));
        int key = Integer.parseInt(words.get(2));
        System.out.println("get clinetIdx keyIdx");
        demo.scenarioClientGet(client,key);
    }
}
