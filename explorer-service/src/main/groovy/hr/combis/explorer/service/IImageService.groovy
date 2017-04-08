package hr.combis.explorer.service

import hr.combis.explorer.model.Image
import hr.combis.explorer.service.result.ImageResult

interface IImageService {
  ImageResult searchImage(byte[] image)
  Image save(Image image)
}
