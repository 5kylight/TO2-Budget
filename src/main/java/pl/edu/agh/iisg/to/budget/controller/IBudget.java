package pl.edu.agh.iisg.to.budget.controller;

import pl.edu.agh.iisg.to.budget.model.Category;

import java.math.BigDecimal;
import java.util.Map;

public interface IBudget {

    //for stats
    Map<Category, BigDecimal> getPlannedAmmountsPerCategory();

}
