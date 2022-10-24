package altera.food.ordering.domain.dao;

import com.alterra.springbootrelationship.domain.common.BaseDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "M_food")
@SQLDelete(sql = "UPDATE M_food SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class food extends BaseDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "food_name", nullable = false)
    private String foodName;

    @ManyToOne
    private restaurant restaurant;

    @ManyToOne
    private Category category;

    @Column(name = "model_year", nullable = false)
    private String modelYear;

    @Column(name = "price", nullable = false)
    private Integer price;
    
}
