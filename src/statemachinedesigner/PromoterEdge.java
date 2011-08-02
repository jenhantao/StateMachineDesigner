package statemachinedesigner;


/**
 *
 * @author Craig LaBoda
 *
 */
  public class PromoterEdge {
        String promoterNumber;   // should be private for good practice
//        Number source;
//        Number target;
        int id;

        public PromoterEdge(String promoterNumber) {
            this.id = edgeCount++;
            this.promoterNumber = promoterNumber;
//            this.source = source;
//            this.target = target;
        }

        public String getWeight()
        {
        	return this.promoterNumber;
        }
        public static void reset() {
            edgeCount=0;
        }


        private static int edgeCount = 0;
   }
