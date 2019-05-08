package net;

public class Request {

    public static final int LOGIN = 0,
            SIGNUP = 1,
            MATCH_LIST = 2,
            CREATE_MATCH = 3,
            LEAVE_MATCH = 4,
            ENTER_MATCH = 5,
            START_MATCH = 6,
            END_MATCH = 7;

    public static String solve(int r) {
        switch (r) {
            case LOGIN:
                return "LOGIN";
            case SIGNUP:
                return "SIGNUP";
            case MATCH_LIST:
                return "MATCH_LIST";
            case CREATE_MATCH:
                return "CREATE_MATCH";
        }
        return String.valueOf(r);
    }
}
