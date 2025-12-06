package utils;

import models.school.kpi.v3.Standard;
import models.school.kpi.v3.TeacherKPIScore;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class CalculatorHelp {

    public static String calculatorKPIStandard(TeacherKPIScore tks){
        AtomicReference<String> standards= new AtomicReference<>("");
        if (tks != null){
            List<Standard> allStandard = Standard.find.all();
            AtomicInteger mx= new AtomicInteger(-1);
            allStandard.forEach(standard -> {
                boolean left=false,right=false;
                if(standard.getLeftOperator().contains(">=")||standard.getLeftOperator().contains("=>")){
                    left=(tks.getFinalScore()>=standard.getLeftLimitScore());
                }
                else if(standard.getLeftOperator().contains("<=")||standard.getLeftOperator().contains("=<")){
                    left=(tks.getFinalScore()<=standard.getLeftLimitScore());
                }

                if(standard.getRightOperator().contains(">=")||standard.getRightOperator().contains("=>")){
                    right=(tks.getFinalScore()>=standard.getRightLimitScore());
                }
                else if(standard.getRightOperator().contains("<=")||standard.getRightOperator().contains("=<")){
                    right=(tks.getFinalScore()<=standard.getRightLimitScore());
                }

                if(((standard.getOp().toLowerCase().contains("and")&&(left&&right))||(standard.getOp().toLowerCase().contains("or")&&(left||right)))&&(standard.getLevel()>= mx.get())){
                    standards.set(standard.getName());
                    mx.set(standard.getLevel());
                }
            });
        }
        return standards.get();
    }
}
