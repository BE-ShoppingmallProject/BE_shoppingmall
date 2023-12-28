package com.github.shoppingmall.shopping_mall.repository.Item;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "ITEM_OPTION")
public class ItemOption implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Integer optionId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="item_id", nullable = false)
    @JsonIgnore
    private Item item;

    @Column(name = "option_content")
    private String optionContent;

    @Column(name = "additional_price")
    private Integer additionalPrice;
}
