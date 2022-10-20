package es.bsc.dataclay.metadata;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.kv.GetResponse;

import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

public class MetadataService implements MetadataServiceAPI {

    private final Client etcdClient;

    public MetadataService(final String etcdHost, final String etcdPort) {
        this.etcdClient = Client.builder().target(etcdHost + ":" + etcdPort).build();
    }

    @Override
    public void autoregisterSL(final String name, final String hostname, final int port) {

        KV kvClient = etcdClient.getKVClient();

        String key = "/storagelocation/" + name;
        ByteSequence keyBytes = ByteSequence.from(key.getBytes());

        String value = new JSONObject()
                .put("name", name)
                .put("hostname", hostname)
                .put("port", port).toString();

        ByteSequence valueBytes = ByteSequence.from(value.getBytes());

        try {
            kvClient.put(keyBytes, valueBytes).get();
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
