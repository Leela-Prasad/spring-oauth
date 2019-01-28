package resources;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="clientErrorInformation")
public class ErrorInformation 
{
	private String message;
	private String uri;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	
}
