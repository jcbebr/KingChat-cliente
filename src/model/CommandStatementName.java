package model;

/**
 *
 * @author José Carlos
 */
public enum CommandStatementName {

    CMM_TYPE("type"),
    CMM_STATUS("status"),
    CMM_CLIENT_ARRAY("clients"),
    CMM_CLIENT_LIST_AVALIABLE("avaliable"),
    CMM_ID("id"),
    CMM_NICK("nick"),
    CMM_EMAIL("email"),
    CMM_OLDEMAIL("oldEmail"),
    CMM_PASS("pass"),
    CMM_BIRTH("birth"),
    CMM_ONLINE("online"),
    CMM_PATH("path"),
    CMM_PORT("port"),
    CMM_MESSAGE("message"),
    CMM_FILE("file");

    private String name;

    private CommandStatementName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
