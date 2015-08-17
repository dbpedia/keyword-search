
package org.dbpedia.keywordsearch.ngramgenerator;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class NGramTestRunner {
   public static void main(String[] args) {
      Result result = JUnitCore.runClasses(NGramTest.class);
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
      System.out.println(result.wasSuccessful());
    } 
}
