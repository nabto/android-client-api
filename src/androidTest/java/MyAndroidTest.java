import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import 	android.support.test.InstrumentationRegistry;
import android.content.Context;
import com.nabto.api.*;


@RunWith(AndroidJUnit4.class)
public class MyAndroidTest {

    @Test
    public void nabtoBasicTests() {
        Context context = InstrumentationRegistry.getTargetContext();
        NabtoStatus status;

        // Start Nabto
        NabtoApi api = new NabtoApi(context);
        status = api.setStaticResourceDir();
        assertEquals(status, NabtoStatus.OK);

        status = api.startup();
        assertEquals(status, NabtoStatus.OK);

        // Login as guest
        Session session = api.openSession("guest", "123456");
        assertEquals(session.getStatus(), NabtoStatus.OK);

        // Make a Nabto request to a device
        String url = "nabto://demo.nabto.net/wind_speed.json?";
        UrlResult result = api.fetchUrl(url, session);
        assertEquals(result.getStatus(), NabtoStatus.OK);

        // Get the response
        String response = new String(result.getResult());
        assertThat(response, containsString("speed_m_s"));

        // Stop Nabto
        status = api.closeSession(session);
        assertEquals(status, NabtoStatus.OK);

        status = api.shutdown();
        assertEquals(status, NabtoStatus.OK);
    }
}
