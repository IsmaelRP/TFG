package project.tfg.ecgscan.data.api;

import java.util.List;

public class ApiResponse {

    private List<LhsItem> lhs;

    public List<LhsItem> getLhs() {
        return lhs;
    }

    public void setLhs(List<LhsItem> lhs) {
        this.lhs = lhs;
    }
}
