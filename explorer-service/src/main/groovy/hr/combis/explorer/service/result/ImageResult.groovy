package hr.combis.explorer.service.result

class ImageResult {
  String name
  BigDecimal latitude
  BigDecimal longitude

  ImageResult(String name, BigDecimal latitude, BigDecimal longitude) {
    this.name = name
    this.latitude = latitude
    this.longitude = longitude
  }
}
