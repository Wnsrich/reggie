package com.wnsrich.reggie.dto;

import com.wnsrich.reggie.entity.Setmeal;
import com.wnsrich.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
