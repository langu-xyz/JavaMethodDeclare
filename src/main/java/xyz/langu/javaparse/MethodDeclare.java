package xyz.langu.javaparse;

/**
 * @author: langu_xyz
 * @DataTime: 2019-11-12
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class MethodDeclare {

    /*return json
     * param path
     * */
    public String declareParseFile(String filename) {
        Map<String, String> declareMap = new HashMap<>();
        File file = new File(filename);
        try {
            CompilationUnit compilationUnit = JavaParser.parse(file);
            TypeDeclaration declaration = compilationUnit.getType(0);
            List<BodyDeclaration> list = declaration.getMembers();
            for (BodyDeclaration bodyDeclaration : list) {
                if (bodyDeclaration.isMethodDeclaration()) {
                    MethodDeclaration declareParse = (MethodDeclaration) bodyDeclaration;
                    declareMap.put(declareParse.getDeclarationAsString(), declareParse.getBody().toString()); // body option 类型待解决
                    //System.out.println(declareParse.getDeclarationAsString());
                    //System.out.println(declareParse.getBody());
                }
            }
            compilationUnit.accept(new VoidVisitorAdapter<Void>() {
                @Override
                public void visit(MethodDeclaration n, Void arg) {
                    //System.out.println(n.getBody().toString());
                    //System.out.println(n.getMetaModel());
                    super.visit(n, arg);
                }
            }, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String declareJsonResult = JSON.toJSONString(declareMap);
        return declareJsonResult;
    }

    public JSONArray declareParseCode(String code) {
        JSONArray declareJsonArray = new JSONArray();
        CompilationUnit compilationUnit = JavaParser.parse(code);
        try {
            TypeDeclaration declaration = compilationUnit.getType(0);
            List<BodyDeclaration> list = declaration.getMembers();
            for (BodyDeclaration bodyDeclaration : list) {
                Map<String, String> declareMap = new HashMap<>();
                if (bodyDeclaration.isMethodDeclaration()) {
                    MethodDeclaration declareParse = (MethodDeclaration) bodyDeclaration;
                    declareMap.put("name", declareParse.getDeclarationAsString());
                    declareMap.put("body", declareParse.getBody().toString());
                }
                JSONObject declareJson = JSONObject.parseObject(JSON.toJSONString(declareMap));
                declareJsonArray.add(declareJson);
            }
            compilationUnit.accept(new VoidVisitorAdapter<Void>() {
                @Override
                public void visit(MethodDeclaration n, Void arg) {
                    super.visit(n, arg);
                }
            }, null);
        }catch (Exception e){
            System.out.println(e);
        }
        return declareJsonArray;
    }
}

