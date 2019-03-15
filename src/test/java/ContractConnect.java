import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.bumo.SDK;
import io.bumo.model.request.ContractCallRequest;
import io.bumo.model.request.ContractGetAddressRequest;
import io.bumo.model.request.operation.ContractInvokeByAssetOperation;
import io.bumo.model.response.ContractCallResponse;
import io.bumo.model.response.ContractGetAddressResponse;
import io.bumo.model.response.result.ContractCallResult;
import io.bumo.model.response.result.data.Signature;

public class ContractConnect {
    public static String set(String key, String value) {
        String url = "http://140.143.38.123:36002";
        SDK sdk = SDK.getInstance(url);

        ContractGetAddressRequest request = new ContractGetAddressRequest();
        request.setHash("7e84c914daa231c7e24219c66771282706b2b588d83d6a6a4f101ca3ab04318b");

// 调用getAddress接口
        ContractGetAddressResponse response = sdk.getContractService().getAddress(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JSON.toJSONString(response.getResult(), true));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }

        String contractAddress = response.getResult().getContractAddressInfos().get(0).getContractAddress();
        System.out.println(contractAddress);
        String input = "";
        JSONObject object = new JSONObject();
        object.put("key", key);
        object.put("value", value);
        System.out.println(object.toJSONString());
        input = object.toJSONString();
        ContractInvokeByAssetOperation operation = BumoKit.contractInvokeByAssetOperation(input, contractAddress);
        String senderAddress = "buQd4TBqSbHw3EoLMnSmH4SJFMkHUtEQbUvz";
        String transactioBlob = BumoKit.seralizeTransaction(sdk, senderAddress, operation);
        String privateKey = "privbzYwbUSCwQZq7eXgu4C9cpqrQD4enXY49V7qUrifc6fCtiPmBhWA";
        Signature[] signatures = BumoKit.signTransaction(privateKey, sdk, transactioBlob);
        String hash = BumoKit.submitTransaction(sdk, transactioBlob, signatures);
        System.out.println(hash);
        return hash;
    }

    public static String get(String key) {
        String url = "http://140.143.38.123:36002";
        SDK sdk = SDK.getInstance(url);

        ContractGetAddressRequest request = new ContractGetAddressRequest();
        request.setHash("7e84c914daa231c7e24219c66771282706b2b588d83d6a6a4f101ca3ab04318b");

// 调用getAddress接口
        ContractGetAddressResponse response = sdk.getContractService().getAddress(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JSON.toJSONString(response.getResult(), true));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }

        String contractAddress = response.getResult().getContractAddressInfos().get(0).getContractAddress();
        System.out.println(contractAddress);
        String input = "";
        JSONObject object = new JSONObject();
        object.put("key", key);
        System.out.println(object.toJSONString());
        input = object.toJSONString();

        ContractCallRequest contractCallRequest = new ContractCallRequest();
        contractCallRequest.setContractAddress(contractAddress);
        //contractCallRequest.setSourceAddress("buQd4TBqSbHw3EoLMnSmH4SJFMkHUtEQbUvz");
        contractCallRequest.setFeeLimit(1000000000L);
        contractCallRequest.setInput(input);
        contractCallRequest.setGasPrice(100000l);
        contractCallRequest.setOptType(2);
        //    contractCallRequest.setInput("invalild");

// 调用call接口
        String data = null;
        ContractCallResponse contractCallResponse = sdk.getContractService().call(contractCallRequest);
        if (contractCallResponse.getErrorCode() == 0) {
            ContractCallResult result = contractCallResponse.getResult();
            data = result.getQueryRets().getString(0);
            //  System.out.println(JSON.toJSONString(result, true));
            // System.out.println(JSON.toJSONString(result.getQueryRets(),true));
        } else {
            System.out.println("error: " + response.getErrorDesc());

        }
        return data;
    }

    public static void main(String[] args) {
        System.out.println(get("key0"));
    }
}
