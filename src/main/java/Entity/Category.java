package Entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Category {

    @Id
    private String categoryId;


    private String size;
    private String gender;


//---------------------------------------------------------
//    @OneToMany
//    private Item item;

//---------------------------------------------------------
    public Category() {
    }
    public Category(String categoryId, String size, String gender) {
        this.categoryId = categoryId;
        this.size = size;
        this.gender = gender;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    @Override
    public String toString() {
        return "Category{" +
                "categoryId='" + categoryId + '\'' +
                ", size='" + size + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
