package hr.combis.explorer.service.impl

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.qpxExpress.QPXExpress
import com.google.api.services.qpxExpress.QPXExpressRequestInitializer
import com.google.api.services.qpxExpress.model.FlightInfo
import com.google.api.services.qpxExpress.model.LegInfo
import com.google.api.services.qpxExpress.model.PassengerCounts
import com.google.api.services.qpxExpress.model.PricingInfo
import com.google.api.services.qpxExpress.model.SliceInfo
import com.google.api.services.qpxExpress.model.SliceInput
import com.google.api.services.qpxExpress.model.TripOption
import com.google.api.services.qpxExpress.model.TripOptionsRequest
import com.google.api.services.qpxExpress.model.TripsSearchRequest
import hr.combis.explorer.service.IFlightsService
import hr.combis.explorer.service.result.Flight
import org.apache.lucene.index.SegmentInfo
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import com.google.api.client.json.JsonFactory

@Service
class FlightsService implements IFlightsService {
  @Value("\${google.api.key}")
  String apiKey = "AIzaSyDaE7_gg2YLS5-NuSCEaSXF1CdK63EwARE"

  final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  @Override
  List<Flight> getFlights() {
    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport()

//    PassengerCounts passengers= new PassengerCounts();
//    passengers.setAdultCount(1);

    List<SliceInput> slices = new ArrayList<SliceInput>();

    SliceInput slice = new SliceInput();
    slice.setOrigin("NYC");
    slice.setDestination("LGA");
    slice.setDate("2017-04-09");
    slices.add(slice);

    TripOptionsRequest request= new TripOptionsRequest();
    request.setSolutions(10);
//    request.setPassengers(passengers);
    request.setSlice(slices);

    def parameters = new TripsSearchRequest()
    parameters.setRequest(request)
    QPXExpress qpXExpress = new QPXExpress.Builder(httpTransport, JSON_FACTORY, null)
            .setApplicationName("API key 2")
            .setGoogleClientRequestInitializer(new QPXExpressRequestInitializer(apiKey)).build()

    def list= qpXExpress.trips().search(parameters).execute();

    List<TripOption> tripResults=list.getTrips().getTripOption();

    String id;

    for(int i=0; i<tripResults.size(); i++){
      //Trip Option ID
      id= tripResults.get(i).getId();
      System.out.println("id "+id);

      //Slice
      List<SliceInfo> sliceInfo= tripResults.get(i).getSlice();
      for(int j=0; j<sliceInfo.size(); j++){
        int duration= sliceInfo.get(j).getDuration();
        System.out.println("duration "+duration);
        List<SegmentInfo> segInfo= sliceInfo.get(j).getSegment();
        for(int k=0; k<segInfo.size(); k++){
          String bookingCode= segInfo.get(k).getBookingCode();
          System.out.println("bookingCode "+bookingCode);
          FlightInfo flightInfo=segInfo.get(k).getFlight();
          String flightNum= flightInfo.getNumber();
          System.out.println("flightNum "+flightNum);
          String flightCarrier= flightInfo.getCarrier();
          System.out.println("flightCarrier "+flightCarrier);
          List<LegInfo> leg=segInfo.get(k).getLeg();
          for(int l=0; l<leg.size(); l++){
            String aircraft= leg.get(l).getAircraft();
            System.out.println("aircraft "+aircraft);
            String arrivalTime= leg.get(l).getArrivalTime();
            System.out.println("arrivalTime "+arrivalTime);
            String departTime=leg.get(l).getDepartureTime();
            System.out.println("departTime "+departTime);
            String dest=leg.get(l).getDestination();
            System.out.println("Destination "+dest);
            String destTer= leg.get(l).getDestinationTerminal();
            System.out.println("DestTer "+destTer);
            String origin=leg.get(l).getOrigin();
            System.out.println("origun "+origin);
            String originTer=leg.get(l).getOriginTerminal();
            System.out.println("OriginTer "+originTer);
            int durationLeg= leg.get(l).getDuration();
            System.out.println("durationleg "+durationLeg);
            int mil= leg.get(l).getMileage();
            System.out.println("Milleage "+mil);

          }

        }
      }

      //Pricing
      List<PricingInfo> priceInfo= tripResults.get(i).getPricing();
      for(int p=0; p<priceInfo.size(); p++){
        String price= priceInfo.get(p).getSaleTotal();
        System.out.println("Price "+price);
      }

    }

    return null
  }

  public static void main(String[] args) {
    new FlightsService().getFlights()
  }
}
