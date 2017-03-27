import com.mongodb.*;
import org.junit.Test;

/**
 * Example test for embedded mongo.
 */
public class ExampleMongoTest extends EmbeddedMongoTest {

    @Override
    public Mongo getMongo() {
        return super.getMongo();
    }

    @Test
    public void testMongo() {
        Mongo mongo = getMongo();
        DBCollection appsCollection = mongo.getDB("local").getCollection("samples");
        DBObject dbObject = new BasicDBObject();
        dbObject.put("key1", "value1");
        appsCollection.insert(dbObject);
        assertEquals(dbObject, appsCollection.findOne());
    }
}
