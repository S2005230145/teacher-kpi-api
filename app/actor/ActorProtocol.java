package actor;

import com.fasterxml.jackson.databind.JsonNode;
import models.admin.ShopAdmin;
import play.libs.Json;
import utils.BalanceParam;

/**
 * Actor对象
 */
public class ActorProtocol {


    public static class CACHE_POS_PRODUCTS {
        public CACHE_POS_PRODUCTS() {
        }
    }

    public static class PRINT_FEIER {
        public String param;
        public String orderNo;
        public int printerType;

        public PRINT_FEIER(String param, String orderNo, int printerType) {
            this.param = param;
            this.orderNo = orderNo;
            this.printerType = printerType;
        }
    }

    public static class BALANCE_LOG {
        public long uid;
        public int itemId;
        public BalanceParam balanceParam;

        public BALANCE_LOG(long uid, int itemId, BalanceParam balanceParam) {
            this.uid = uid;
            this.itemId = itemId;
            this.balanceParam = balanceParam;
        }
    }

    public static class CACHE_PAY_RESULT {
        public CACHE_PAY_RESULT() {
        }
    }

    public static class HANDLE_PAY_RESULT {
        public String orderNo;
        public String txNo;
        public String payDetail;

        public HANDLE_PAY_RESULT(String orderNo, String txNo, String payDetail) {
            this.orderNo = orderNo;
            this.txNo = txNo;
            this.payDetail = payDetail;
        }
    }
 

    public static class CANCEL_ORDER {
        public long orderId;

        public ShopAdmin operator;

        public CANCEL_ORDER(ShopAdmin operator, long orderId) {
            this.operator = operator;
            this.orderId = orderId;
        }
    }

    public static class AGREE_CANCEL_ORDER {
        public long orderId;
        public ShopAdmin operator;

        public AGREE_CANCEL_ORDER(long orderId, ShopAdmin operator) {
            this.orderId = orderId;
            this.operator = operator;
        }
    }

    public static class PART_RETURN {
        public ShopAdmin operator;
        public JsonNode node;

        public PART_RETURN(ShopAdmin operator, JsonNode node) {
            this.operator = operator;
            this.node = node;
        }
    }

    public static class HANDLE_WEPAY_REFUND {
        public String result;
        public long orgId;

        public HANDLE_WEPAY_REFUND(String result, long orgId) {
            this.result = result;
            this.orgId = orgId;
        }
    }


    public static class ORDER_RETURN_HANDLE_REFUND {
        public long orderReturnId;
        public long orderId;
        public ShopAdmin operator;

        public ORDER_RETURN_HANDLE_REFUND(ShopAdmin operator, long orderReturnId, long orderId) {
            this.operator = operator;
            this.orderReturnId = orderReturnId;
            this.orderId = orderId;
        }
    }

    public static class NOTIFY_ORDER {

        public NOTIFY_ORDER() {
        }
    }


    public static class STABLE_ACCESS_TOKEN {

        public STABLE_ACCESS_TOKEN() {
        }
    }

    public static class LISTEN_STABLE_ACCESS_TOKEN {

        public LISTEN_STABLE_ACCESS_TOKEN() {
        }
    }


    public static class REPORT_DAY {
        public REPORT_DAY() {
        }
    }

    public static class REPORT_LAST_DAY {
        public REPORT_LAST_DAY() {
        }
    }

    public static class AUTO_TAKE_ORDER {
        public AUTO_TAKE_ORDER() {
        }
    }

    public static class CABINET_BOX_UPDATE_STATUS {
        public JsonNode jsonNode;

        public CABINET_BOX_UPDATE_STATUS(JsonNode jsonNode) {
            this.jsonNode = jsonNode;
        }
    }
}
