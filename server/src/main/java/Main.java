import chess.*;
import dataaccess.DatabaseManager;

public class Main {
    public static void main(String[] args) throws Exception {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT 1+1")) {
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        int out = rs.getInt(1);
                        System.out.println(out);
                    }
                }
            }
        }
    }
}