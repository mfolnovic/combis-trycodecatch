package hr.combis.explorer.service.impl

import com.google.cloud.vision.spi.v1.ImageAnnotatorClient
import com.google.cloud.vision.v1.AnnotateImageRequest
import com.google.cloud.vision.v1.Feature
import com.google.cloud.vision.v1.Image
import com.google.protobuf.ByteString
import hr.combis.explorer.dao.IImageRepository
import hr.combis.explorer.service.IImageService
import hr.combis.explorer.service.result.ImageResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.nio.file.Files
import java.nio.file.Paths

@Service
class ImageService implements IImageService {
//  @Value("${google.vision.api.key}") String apiKey
  private IImageRepository imageRepository

  @Autowired
  public ImageService(IImageRepository imageRepository) {
    this.imageRepository = imageRepository
  }

  @Override
  ImageResult searchImage(byte[] bytes) {
    def vision = ImageAnnotatorClient.create()

    def image = ByteString.copyFrom(bytes)

    def requests = []
    def img = Image.newBuilder().setContent(image).build()
    def feat1 = Feature.newBuilder().setType(Feature.Type.WEB_DETECTION).build()
    def feat2 = Feature.newBuilder().setType(Feature.Type.LANDMARK_DETECTION).build()
    def request = AnnotateImageRequest.newBuilder()
            .addFeatures(feat1)
            .addFeatures(feat2)
            .setImage(img)
            .build()
    requests.add(request)

    // Performs label detection on the bytes file
    def response = vision.batchAnnotateImages(requests)
    def responses = response.getResponsesList()

    if (responses.isEmpty() || responses[0].webDetection.webEntitiesCount == 0) {
      return null
    }

    def entity = responses[0].webDetection.webEntitiesList[0].description

    def latitude = null
    def longitude = null

    if (responses[0].landmarkAnnotationsCount > 0 && responses[0].landmarkAnnotationsList[0].locationsCount > 0) {
      def latlng = responses[0].landmarkAnnotationsList[0].locationsList[0].latLng
      latitude = BigDecimal.valueOf(latlng.latitude)
      longitude = BigDecimal.valueOf(latlng.longitude)
    }

    return new ImageResult(entity, latitude, longitude)
  }

  @Override
  hr.combis.explorer.model.Image save(hr.combis.explorer.model.Image image) {
    return imageRepository.save(image)
  }
}
