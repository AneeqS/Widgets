import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.StringTokenizer;

public class Widgets {
    //These variables hold the amount of inventory that we receive and the amount we are to sell.
    //These are made private because to restrict access to main.
    private static int toRecieve = 0, toSell = 0;
    private static double price1 = 0.0;

    //These are the data structures that hold the inventory we receive and the prices that go with the inventory.
    private static Stack<Integer> widgets = new Stack<Integer>();
    private static Stack<Double> price = new Stack<Double>();
    private static Queue<Integer> back_Orders = new LinkedList();

    //This Function adds a 40% increase in the price.
    private static double calc_Price(double y){
        return y = y + Math.round((((y*40)/100)*100.0)/100.0);
    }

    //This Function prints the receipt for the customer.
    private static void print_Receipt(int x, double y){
        System.out.println("Customer Receipt ");
        System.out.println(x + " @ $" + y + " = " + Math.round((x*y)*100.0)/100.0);
        System.out.println();
    }

    //This Function prints the Bookkeepers data, where it show us how much we would have made if we didn't charge an extra 40%
    private static void print_Bookkeeper_Data(int x, double y){
        System.out.println("Bookkeeper's data: ");
        System.out.println("Acutal Cost:");
        System.out.println(x + " @ $" + y + " = " + Math.round((x*y)*100.0)/100.0);
        System.out.println();

    }

    public static void main(String[] args) {

        int x = 0;
        double y = 0.0;
        BufferedReader reader = null;
        String temp = "";//This variable just holds the first letter of the input line.

        try {

            File file = new File("transactions.txt");
            reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {

                StringTokenizer st = new StringTokenizer(line, " ");

                while(st.hasMoreTokens()){
                    //If we are receiving shipment, push it into a stack and push the price into a separate price.
                    if(line.charAt(0) == 'R'){

                        temp = st.nextToken();
                        toRecieve = Integer.parseInt(st.nextToken());
                        price1 = Double.parseDouble(st.nextToken());
                        widgets.push(toRecieve);
                        price.push(price1);
                    }
                    //If we have to sell from our inventory
                    if(line.charAt(0) == 'S'){

                        temp = st.nextToken();
                        //toSell hold the integer amount of the order we received
                        toSell = Integer.parseInt(st.nextToken());

                        //If we have back orders, this updates the toSell variable to sell the back orders first.
                        if(!(back_Orders.isEmpty())){
                            toSell = back_Orders.peek();
                            back_Orders.remove();

                        }

                        //If we have enough to sell in our inventory, sell the amount and update the stack
                        if(toSell <= widgets.peek()){
                            y = calc_Price(price.peek());
                            x = widgets.pop() - toSell;
                            widgets.push(x);
                            print_Receipt(toSell, y);
                            print_Bookkeeper_Data(toSell, price.peek());
                        }

                        //If we don't have enough in our inventory, sell what we have and add the remaining balance to a back order.
                        if(toSell > widgets.peek()){
                            if(widgets.size() == 1){
                                back_Orders.add(toSell);
                                break;

                            }
                            if(widgets.size() > 1){
                                y = calc_Price(price.peek());
                                x = widgets.pop();
                                print_Receipt(x, y);
                                print_Bookkeeper_Data(x, price.peek());
                                toSell = toSell - x;
                                x = (widgets.pop()) - toSell;
                                widgets.push(x);
                                y = calc_Price(price.peek());
                                print_Receipt(toSell, y);
                                print_Bookkeeper_Data(toSell, price.peek());

                            }
                        }
                    }
                }
            }
            //Printed this out data checking, to keep track of what's happening in each data structure.
            System.out.println(widgets);
            System.out.println(price);
            System.out.println(back_Orders);

        } catch (IOException e) {
            e.printStackTrace();

        } finally {

            try {
                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
