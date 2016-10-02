import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * Created by nitix on 29/09/2016.
 */
public class Vote {

    public static final long SCORE_MODIFIER = 457;
    public static final int UNE_SEMAINE = 604800;

    public static void ajoutVote(Jedis conn, String utilisateur, String articleId, boolean plus) {
        if (conn.smembers("selectionne:" + articleId).contains(utilisateur)) {
            return;
        }
        conn.sadd("selectionne:" + articleId, utilisateur);
        conn.hincrBy(articleId, "nbvotes", 1);
        if(plus)
            conn.zincrby("score:article", SCORE_MODIFIER, articleId);
        else
            conn.zincrby("score:article", -SCORE_MODIFIER, articleId);
    }

    public static void initVote(Jedis conn, String utilisateur, String articleId, long date) {
        String articleSelectionne = "selectionne:" + articleId;
        conn.sadd(articleSelectionne, utilisateur);
        conn.expire(articleSelectionne, UNE_SEMAINE);
        conn.zadd("score:article", date + Vote.SCORE_MODIFIER, articleId);
    }

    public static double getScore(Jedis conn, String articleId) {
        return conn.zscore("score:article", articleId);
    }

    public static Set<String> getTredingArticles(Jedis conn) {
        return conn.zrevrange("score:article", 0, 9);
    }

}

