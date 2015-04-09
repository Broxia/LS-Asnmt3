package src;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

import services.Countries;
import services.CountryInfo;

public class Server extends Application{
	public static void main(String [] args) throws Exception{
		Component component = new Component();
		component.getServers().add(Protocol.HTTP, 8080);
		
		final Server app = new Server();
		component.getDefaultHost().attach(app);
		
		component.start();
	}
	
	@Override
	public synchronized Restlet createInboundRoot() {
		Router router = new Router(getContext());

        router.attach("/countries/{countryName}", CountryInfo.class);
        router.attach("/countries", Countries.class);
       
        return router;
	}
}
