import ui.*;
public class Main {
    private static int port = 8080;

    public Main() {
    }

    public static void main(String[] args) throws Exception{
        System.out.println("Welcome to 240 Chess. Type \"help\" to get started");
        if(args.length == 2) {
            port = Integer.parseInt(args[1]);
        }
        else{
            Util.setPort(port);
        }
        Util util = new Util(port);
        Ui.displayPreLoginUI();
    }
}
