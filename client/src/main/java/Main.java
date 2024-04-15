import ui.*;
public class Main {

    public Main() {
    }

    public static void main(String[] args) throws Exception{
        System.out.println("Welcome to 240 Chess. Type \"help\" to get started");
        if(args.length == 2) {
            Util.port = Integer.parseInt(args[1]);
        }
        Ui.displayPreLoginUI();
    }
}
