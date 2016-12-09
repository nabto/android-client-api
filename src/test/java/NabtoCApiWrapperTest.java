import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.core.IsInstanceOf.*;
import static org.hamcrest.core.StringContains.*;
import com.nabto.api.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import android.content.res.AssetManager;
import android.util.Log;
import java.io.File;
import java.io.IOException;

import org.powermock.api.mockito.PowerMockito;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class NabtoCApiWrapperTest {

    @Test
    public void nabtoBasicTests() {
        String email = "guest";
        String password = "";
        String url = "nabto://demo.nabto.net/wind_speed.json?";

        /* Check nabto library version */
        String version = NabtoCApiWrapper.nabtoVersion();
        //assertThat(version, containsString("2."));

        /* Initialize the Nabto Client API with path to assets */
        NabtoStatus status = NabtoStatus.fromInteger(NabtoCApiWrapper.nabtoStartup("src/main/assets/share/nabto"));
        assertEquals(status, NabtoStatus.OK);

        /* Open a session with guest */
        Session session = NabtoCApiWrapper.nabtoOpenSession(email, password);
        assertEquals(session.getStatus(), NabtoStatus.OK);
        assertThat(session.toString(), instanceOf(String.class));

        /* Fetch a JSON ressource from URL */
        UrlResult result = NabtoCApiWrapper.nabtoFetchUrl(url, session.getSession());
        String jsonString = new String(result.getResult());
        assertEquals(result.getStatus(), NabtoStatus.OK);
        assertThat(jsonString, containsString("speed_m_s"));

        /* Close session again */
        status = NabtoStatus.fromInteger(NabtoCApiWrapper.nabtoCloseSession(session.getSession()));
        assertEquals(status, NabtoStatus.OK);

        /* Shutdown Nabto */
        status = NabtoStatus.fromInteger(NabtoCApiWrapper.nabtoShutdown());
        assertEquals(status, NabtoStatus.OK);
    }

    @Mock
    Context mockContext;

    @Mock
    ApplicationInfo mockApplicationInfo;

    @Mock
    AssetManager mockAssetManager;

    @Before
    public void setup() {
        when(mockContext.getApplicationInfo()).thenReturn(mockApplicationInfo);
        when(mockContext.getFilesDir()).thenReturn(new File("src/main/assets"));
        PowerMockito.mockStatic(Log.class);
        mockApplicationInfo.nativeLibraryDir = "";
        try {
            when(mockAssetManager.list("share")).thenReturn(new String[0]);
        } catch(IOException e) {};
        when(mockContext.getAssets()).thenReturn(mockAssetManager);
    }

    @Test
    public void nabtoApiTests() {


        // Start Nabto
        NabtoApi api = new NabtoApi(mockContext);
        NabtoStatus status = api.setStaticResourceDir();
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