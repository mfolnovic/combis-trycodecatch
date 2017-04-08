package hr.combis.explorer.service.impl

import com.google.cloud.vision.spi.v1.ImageAnnotatorClient
import com.google.cloud.vision.v1.AnnotateImageRequest
import com.google.cloud.vision.v1.Feature
import com.google.cloud.vision.v1.Feature.Type
import com.google.cloud.vision.v1.Image
import com.google.protobuf.ByteString
import hr.combis.explorer.service.IImageService

class ImageService implements IImageService {
  @Override
  String findTopEntity(byte[] bytes) {
    def vision = ImageAnnotatorClient.create()

    def image = ByteString.copyFrom(bytes)

    def requests = []
    def img = Image.newBuilder().setContent(image).build()
    def feat = Feature.newBuilder().setType(Type.WEB_DETECTION).build()
    def request = AnnotateImageRequest.newBuilder()
            .addFeatures(feat)
            .setImage(img)
            .build()
    requests.add(request)

    // Performs label detection on the bytes file
    def response = vision.batchAnnotateImages(requests)
    def responses = response.getResponsesList()

    if (responses.isEmpty() || responses[0].webDetection.webEntitiesCount == 0) {
      return null
    }

    def entity = responses[0].webDetection.webEntitiesList[0]

    return entity.description
  }
}
