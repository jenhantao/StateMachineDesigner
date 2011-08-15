package statemachinedesigner;


/**
 *
 * @author Craig LaBoda
 *
 */
  public class PromoterEdge {
        String promoterNumber;   // should be private for good practice
        Integer source;
        Integer dest;
        int id=0;

        public PromoterEdge(String promoterNumber, int source, int dest) {
            this.id = edgeCount++;
            this.promoterNumber = promoterNumber;
            this.source = source;
            this.dest = dest;
        }

        public String getWeight()
        {
        	return this.promoterNumber;
        }
        public static void reset() {
            edgeCount=0;
        }

        public Number getSource() {
            return source;
        }
        public Number getDest() {
            return dest;
        }
       


        private static int edgeCount = 0;
   }
