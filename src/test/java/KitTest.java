import io.bumo.SDK;
import io.bumo.model.request.operation.BaseOperation;
import io.bumo.model.response.result.data.Signature;

public class KitTest {
    public static void main(String[] args) {
        String url = "http://140.143.38.123:36002";
        SDK sdk = SDK.getInstance(url);
        String payload = "\"use strict\";\n" +
                "let i= 1;\n" +
                "function init(input)\n" +
                "{\n" +
                "    i=0;\n" +
                "    /* init whatever you want */ return;\n" +
                "}\n" +
                "\n" +
                "function query(input)\n" +
                "{\n" +
                "    /* do whatever you want */\n" +
                "    let keyValue = JSON.parse(input).key;\n" +
                "    let data = storageLoad(keyValue);\n" +
                "    if(data === false)\n" +
                "    {\n" +
                "        return 'false';\n" +
                "    }\n" +
                "    return data;\n" +
                "}\n" +
                "\n" +
                "function main(input)\n" +
                "{\n" +
                "    /* query whatever you want, but should return what you query */\n" +
                "    \n" +
                "    let obj =JSON.parse(input);\n" +
                "    let key = obj.key;\n" +
                "    let value = obj.value;\n" +
                "    \n" +
                "    storageStore(key,value);\n" +
                "    return ;\n" +
                "}";
        String senderAddress = "buQd4TBqSbHw3EoLMnSmH4SJFMkHUtEQbUvz";

        BaseOperation operation = BumoKit.conctractCreateOperation(payload,senderAddress);
        String transactioBlob = BumoKit.seralizeTransaction(sdk,senderAddress,operation);
        String privateKey = "privbzYwbUSCwQZq7eXgu4C9cpqrQD4enXY49V7qUrifc6fCtiPmBhWA";
        Signature[] signatures = BumoKit.signTransaction(privateKey,sdk,transactioBlob);
        String hash = BumoKit.submitTransaction(sdk,transactioBlob,signatures);
        System.out.println(hash);
    }
}
