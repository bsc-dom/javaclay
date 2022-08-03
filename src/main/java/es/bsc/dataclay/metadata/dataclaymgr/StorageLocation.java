package es.bsc.dataclay.metadata.dataclaymgr;

public class StorageLocation {

    private String name;

    private String hostname;

    private int port;

    public StorageLocation(final String name, final String hostname, final int port) {
        this.name = name;
        this.hostname = hostname;
        this.port = port;
    }

}
