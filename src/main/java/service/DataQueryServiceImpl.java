package service;

import api.*;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.parser.cqn.CQNParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.stream.Collectors;
import static com.googlecode.cqengine.query.QueryFactory.*;
import static service.DataQueryServiceImpl.OPERATORS.*;

public class DataQueryServiceImpl implements DataQueryService {

    private static final Logger logger = LoggerFactory.getLogger(DataQueryServiceImpl.class);
    private static Map<String, SimpleAttribute<Item, Comparable>> attributes = DynamicIndexer.generateAttributesForPojo(Item.class);
    private static IndexedCollection<Item> items = DynamicIndexer.newAutoIndexedCollection(attributes.values());

    private String getOperatorName(String operator){
        operator = operator.toLowerCase();
        if(operator.contains("_")){
            return camelcasify(operator);
        }
        return operator;
    }

    private boolean isValidQuery(String query){

        for(OPERATORS op: values()){
            if(query.toLowerCase().startsWith(op.toString().toLowerCase())){
                return true;
            }
        }
        return false;
    }

    private List<Item> executeQuery(String query) throws QueryParseException {
        List<Item> output = new ArrayList<>();
        try{
            // return all
            if(query.isEmpty()){
                output.addAll(items);
                return output;
            }

            if(! isValidQuery(query)) {
                throw new QueryParseException("Invalid Operator input");
            }

            query = prepareQuery(query);
            logger.info("Running Query: " +query);

            CQNParser<Item> parser = CQNParser.forPojoWithAttributes(Item.class, attributes);
            parser.retrieve(items,query).stream().forEach(output::add);
            return output ;
        }catch (Exception e){
            throw new QueryParseException("Non readable file",e);
        }


    }

    private String camelcasify(String in) {
        StringBuilder sb = new StringBuilder();
        boolean capitalizeNext = false;
        for (char c : in.toCharArray()) {
            if (c == '_') {
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    sb.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    private String parseSingleOperator(String query){
        StringBuilder sb = new StringBuilder();
        int opnBrktIdx = query.indexOf('(');
        int clsBrktIdx = query.indexOf(')');
        int commaIdx = query.indexOf(',');

        String operator = query.substring(0,opnBrktIdx);
        String operatorName = getOperatorName(operator);
        String property = query.substring((opnBrktIdx+1),commaIdx);
        String value = query.substring((commaIdx+1),clsBrktIdx);

        sb.append(operatorName);
        sb.append("(");
        sb.append(String.format("\"%s\"",property));
        sb.append(",");
        sb.append(value);
        sb.append(")");
        return sb.toString();
    }

    private String parseMultiOprator(String query){
        StringBuilder sb = new StringBuilder();

        int opnBrktIdxFirst = query.indexOf('(');
        int clsBrktIdxLast = query.lastIndexOf(')');

        String parentOperator = query.substring(0,opnBrktIdxFirst);
        sb.append(getOperatorName(parentOperator));
        sb.append("(");

        List<String> operatorExpressions = Arrays.asList(query.substring((opnBrktIdxFirst+1), clsBrktIdxLast).split("\\),"));
        for(String ex: operatorExpressions){

            if(!ex.endsWith(")")){
                sb.append(parseSingleOperator(ex + ")"));
            }
            else{
                sb.append(parseSingleOperator(ex));
            }
            if(operatorExpressions.indexOf(ex) != (operatorExpressions.size() -1) ){
                sb.append(",");
            }

        }
        sb.append(")");
        return sb.toString();
    }


    private String prepareQuery(String query){
        if(query.contains("))")){
            return parseMultiOprator(query);
        }
        return parseSingleOperator(query);
    }


    public List<Item> query(String query) throws QueryParseException {
        try{
            return executeQuery(query);
        } catch (Exception e){
            throw (QueryParseException) e.getCause();
        }


    }

    public void save(Item Item) throws Exception {
        logger.info("Checking if item exists...");
        try{
            Set<Item> results = items.retrieve(equal(attributes.get("id"), Item.getId())).stream().collect(Collectors.toSet());

            if(results.isEmpty()){
                logger.info("Ready to insert...");
                items.add(Item);
                logger.info("Ok");
                return;
            }
            if(results.size() > 1){
                logger.info("Warning: Found duplications:");
                results.forEach(System.out::println);

                logger.info("Save action is canceled.");
                return;
            }
            logger.info("Item was found in store, updating...");
            Item itemToUpdate = (Item) results.toArray()[0];
            boolean isUpdated;
            isUpdated = items.update(Collections.singletonList(itemToUpdate), Collections.singletonList(Item));

            logger.info(String.format("Update State: %s", isUpdated));
        }catch (Exception e){
            throw (QueryParseException) e.getCause();
        }




    }

    public enum OPERATORS {
        EQUAL,
        GREATER_THAN,
        LESS_THAN,
        AND,
        OR,
        NOT
    }
}
