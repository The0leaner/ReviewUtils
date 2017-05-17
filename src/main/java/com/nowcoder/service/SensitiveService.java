
package com.nowcoder.service;

import com.nowcoder.util.WendaUtil;
import com.sun.org.apache.xml.internal.security.Init;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Service
public class SensitiveService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(WendaUtil.class);


    @Override
    public void afterPropertiesSet() throws Exception {

        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineText;
            while ((lineText = bufferedReader.readLine()) != null){
                addWord(lineText.trim());
            }
            read.close();
        }catch (Exception e) {

            logger.error("读取敏感词文件失败" + e.getMessage());
        }
    }
//增加关键词
    private void addWord (String lineText){
        TrieNode tempNode = rootNode;
        for (int i = 0 ; i < lineText.length() ; ++i) {
            Character c = lineText.charAt(i);

            TrieNode node = tempNode.getSubNode(c);

            if (node == null){
                node = new TrieNode();
                tempNode.addSubNode(c , node);
            }

            tempNode = node;

            if (i == lineText.length() - 1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    private class TrieNode {

        //是不是关键词词尾
        private boolean end = false;

        //当前节点下的所有子节点
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public void addSubNode (Character key , TrieNode value) {
            subNodes.put(key , value);
        }

        TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        boolean isKeywordEnd () {
            return end;
        }

        void setKeywordEnd(boolean end){

            this.end = end;
        }
    }
    private TrieNode rootNode = new TrieNode();

    /**
     * 判断是否是一个符号
     * @param c
     * @return
     */
    private boolean isSymbol (char c) {
        int ic = (int) c;
        // 0x2E80-0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    public String filter (String text) {
        if (StringUtils.isBlank(text)) {
            return  text;
        }

        String replacement = "***";
        TrieNode tempNode = rootNode;
        int begin = 0;
        int position = 0;

        StringBuilder result = new StringBuilder();

        while (position < text.length()) {
            char c = text.charAt(position);
             //如果是非法词汇应该调空掉
            if (isSymbol(c)) {
                if (tempNode == rootNode) {
                    result.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }

            tempNode = tempNode.getSubNode(c);

            if (tempNode == null){
                result.append(text.charAt(begin));
                position = begin + 1;
                //begin + 1原因在于这里没有发现敏感词
                begin = position;
                tempNode = rootNode;
                //回到tree的根节点
            }else if (tempNode.isKeywordEnd()) {
                //发现敏感词
                result.append(replacement);
                //可以在发现敏感词的后面+1，因为前面都已经打码了
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            }else {
                ++position;
            }
        }
        result.append(text.substring(begin));
        return result.toString();
    }

}
