package model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CategoryTm extends RecursiveTreeObject<CategoryTm> {
    private String categoryId;
    private String size;
    private String gender;
}
