package controller;

import controller.Vote;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by nitix on 29/09/2016.
 */
public class VoteTest {

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
    public void testInitVote() {
        Jedis jedis = new Jedis(host, port);
        jedis.select(db);
        String utilisateur = "Personne 1";
        String articleId = "article:test-init-vote";
        long now = System.currentTimeMillis() / 1000;
        Vote.initVote(jedis, utilisateur, articleId, now);
        assertEquals(now + Vote.SCORE_MODIFIER, jedis.zscore("score:article", articleId), 5);
        assertTrue(jedis.smembers("selectionne:"+articleId).contains("Personne 1"));
    }

    @Test
    public void testAjoutVote() {
        Jedis jedis = new Jedis(host, port);
        jedis.select(db);
        String utilisateur = "Personne 1";
        String articleId = "article:test-ajout-vote";
        long now = System.currentTimeMillis() / 1000;
        Vote.initVote(jedis, utilisateur, articleId, now);
        Vote.ajoutVote(jedis, utilisateur, articleId, true);
        assertEquals(now + Vote.SCORE_MODIFIER, jedis.zscore("score:article", articleId), 1);
        Vote.ajoutVote(jedis, utilisateur+1, articleId, true);
        assertEquals(now + Vote.SCORE_MODIFIER * 2, jedis.zscore("score:article", articleId), 1);
        Vote.ajoutVote(jedis, utilisateur+2, articleId, false);
        assertEquals(now + Vote.SCORE_MODIFIER, jedis.zscore("score:article", articleId), 1);
    }
}
