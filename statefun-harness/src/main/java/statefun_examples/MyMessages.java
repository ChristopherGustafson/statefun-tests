package statefun_examples;

public class MyMessages {

    public static final class InputMsg{
        private final String userId;
        private final String message;

        public InputMsg(String userId, String message){
            this.userId = userId;
            this.message = message;
        }

        String getUserId(){ return userId; }

        String getMessage() {
            return message;
        }
    }

    public static final class InternalMessage{
        private final String userId;
        private final String message;

        InternalMessage(String userId, String message){
            this.userId = userId;
            this.message = message;
        }

        public String getUserId() {
            return userId;
        }

        public String getMessage() {
            return message;
        }
    }

    static final class OutputMsg{
        private final String userId;
        private final String message;

        OutputMsg(String userId, String message){
            this.userId = userId;
            this.message = message;
        }

        String getUserId(){
            return getUserId();
        }

        String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return "OutputMsg{" +
                    "userId='" + userId + '\'' +
                    ", message='" + message + '\'' +
                    '}';
        }
    }
}
