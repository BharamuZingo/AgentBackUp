package app.zingo.com.agentapp.Model;

/**
 * Created by User on 23-09-2017.
 */

public class HotelService {

    private int serviceImage;
    private String serviceName;

    public HotelService(int serviceImage, String serviceName)
    {
        this.serviceImage = serviceImage;
        this.serviceName = serviceName;
    }

    public void setServiceImage(int serviceImage) {
        this.serviceImage = serviceImage;
    }

    public int getServiceImage() {
        return serviceImage;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }
}

