package api.model.order.requests;

public class OrderCreationRequestBody {

    private String ingredients;

    public OrderCreationRequestBody(String ingredients) {
        this.ingredients = ingredients;
    }

    public OrderCreationRequestBody() {}

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}
