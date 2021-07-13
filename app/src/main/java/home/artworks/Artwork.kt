package home.artworks

class Artwork {
    var image: Int? = null
    var title: String? = null
    var price: String? = null


    constructor(image: Int, title: String, price: String) {
        this.image = image
        this.title = title
        this.price = price
    }
}