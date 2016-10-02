import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import redis.clients.jedis.Jedis;

public class BasicTest{

    private static final String host = "localhost";
    private static final int port = 6379;
    private static final int db = 2;

    @BeforeClass
    public static void cleanUp(){
        Jedis jedis = new Jedis(host, port);
        jedis.select(db);
        if (db != 0){
            jedis.flushDB();
        }
    }

    @Test
    public void testConnexion(){
        Jedis jedis = new Jedis(host, port);
        jedis.select(db);
        assertEquals(jedis.ping(), "PONG");
    }

    @Test
    public void testCle(){
        Jedis jedis = new Jedis(host, port);
        jedis.select(db);
        assertNull(jedis.get("macle"), null);
        jedis.set("macle", "42");
        assertEquals(jedis.get("macle"), "42");
    }
}