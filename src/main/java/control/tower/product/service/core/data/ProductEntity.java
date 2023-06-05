package control.tower.product.service.core.data;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name="product")
public class ProductEntity implements Serializable {

    private static final long serialVersionUID = 789654123987456322L;

    @Id
    @Column(unique = true)
    private String productId;
    private String name;
}
