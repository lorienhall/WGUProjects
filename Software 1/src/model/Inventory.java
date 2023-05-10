package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/** This class holds all the information for the part and product observable lists. */
public class Inventory
{
    /** Part and products observable list. */
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();


    /** These two methods add a part or product to the respective observable list. */
    public static void addPart (Part part)
    {
        allParts.add(part);
    }
    public static void addProduct (Product product)
    {
        allProducts.add(product);
    }

    /** These two methods get all the parts or products in the respective list. */
    public static ObservableList<Part> getAllParts()
    {
        return allParts;
    }
    public static ObservableList<Product> getAllProduct()
    {
        return allProducts;
    }

    /** These methods search their respective observable list for a part/product by id and by name. */
    public static ObservableList<Part> lookUpPart(String partName)
    {
        ObservableList<Part> namedParts = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.getAllParts();

        for(Part p : allParts)
        {
            if (p.getName().toLowerCase().contains(partName))
            {
                namedParts.add(p);
            }
        }
        return namedParts;
    }

    public static Part lookupPart(int partId)
    {
        ObservableList<Part> allParts = Inventory.getAllParts();

        for (Part p : allParts) {
            if (p.getId() == partId) {
                return p;
            }
        }
        return null;
    }

    public static ObservableList<Product> lookUpProduct(String productName)
    {
        ObservableList<Product> namedProduct = FXCollections.observableArrayList();
        ObservableList<Product> allParts = getAllProduct();

        for(Product p : allParts)
        {
            if (p.getName().toLowerCase().contains(productName))
            {
                namedProduct.add(p);
            }
        }
        return namedProduct;
    }

    public static Product lookupProduct(int productId)
    {
        ObservableList<Product> allParts = getAllProduct();

        for (Product p : allParts) {
            if (p.getId() == productId) {
                return p;
            }
        }
        return null;
    }

    /** These two methods update a part or a product. */
    public static boolean updatePart(Part part)
    {
        for (Part parts : Inventory.getAllParts())
        {
            if(parts.getId() == part.getId())
            {
                parts.setName(part.getName());
                parts.setPrice(part.getPrice());
                parts.setStock(part.getStock());
                parts.setMin(part.getMin());
                parts.setMax(part.getMax());
                if (parts instanceof InHouse)
                {
                    ((InHouse) parts).setMachineId(((InHouse) part).getMachineId());
                }
                if (parts instanceof Outsourced)
                {
                    ((Outsourced) parts).setCompanyName(((Outsourced) part).getCompanyName());
                }
                return true;
            }
        }
        return false;
    }

    public static boolean updateProduct(Product product)
    {
        for (Product products : Inventory.getAllProduct())
        {
            if(products.getId() == product.getId())
            {
                products.setName(product.getName());
                products.setPrice(product.getPrice());
                products.setStock(product.getStock());
                products.setMin(product.getMin());
                products.setMax(product.getMax());
                return true;
            }
        }
        return false;
    }

    /** These two methods delete a part or product from their observable list. */
    public static boolean deletePart(Part part)
    {
        allParts.remove(part);
        return false;
    }

    public static boolean deleteProduct(Product product)
    {
        allProducts.remove(product);
        return false;
    }




}