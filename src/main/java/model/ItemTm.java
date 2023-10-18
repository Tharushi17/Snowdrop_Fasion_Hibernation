package model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ItemTm extends RecursiveTreeObject<ItemTm> {
    private String itemCode;
    private String description;
    private String supplierId;
    private int stock;
    private double buyingPrice;
    private double sellingPrice;
    private String categoryId;
    private String type;
    private double profit;


}


