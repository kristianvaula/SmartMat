import { expect, test } from 'vitest';
import axiosInstance from "~/service/AxiosInstance";
import MockAdapter from 'axios-mock-adapter';
import { eatGrocery, getGroceriesDTOs, getGroceriesByFridge, createGrocery, deleteGrocery, trashGrocery, removeGrocery, updateGrocery } from "./GroceryService";

const mockAxios = new MockAdapter(axiosInstance);

describe('GroceryService', () => {
    beforeEach(() => {
      mockAxios.reset();
    });
  
    describe('getGroceriesByFridge', () => {
      it('should get groceries by fridge', async () => {
        const fridgeId = 123;
        const expectedResponse = {
          data: [{ name: 'Apple', quantity: 2 }]
        };
        mockAxios.onGet(`/api/refrigerator/grocery/${fridgeId}`).reply(200, expectedResponse.data);
  
        const response = await getGroceriesByFridge(fridgeId);
  
        expect(response.data).toEqual(expectedResponse.data);
        expect(mockAxios.history.get[0].url).toEqual(`/api/refrigerator/grocery/${fridgeId}`);
      });
    });
  
    describe('createGrocery', () => {
      it('should create a grocery', async () => {
        const fridgeId = 123;
        const grocery = {
            id: 1,
            name: "test",
            description: "string",
            groceryExpiryDate: 1,
            subCategory: {
                id : 1,
                name :"test",
                category : {
                    id : 1,
                    name : "test",
                }
            },
        };
        const unit = {id : 1, weight : 100, name : "dl"};
        const quantity = 2;
        const expectedResponse = {
          data: { id: 456, name: 'Apple', unit: { name: 'Count' }, quantity: 2 }
        };
        mockAxios.onPost(`/api/refrigerator/grocery/new/${fridgeId}`).reply(200, expectedResponse.data);
  
        const response = await createGrocery(fridgeId, grocery, unit, quantity);
  
        expect(response.data).toEqual(expectedResponse.data);
        expect(mockAxios.history.post[0].url).toEqual(`/api/refrigerator/grocery/new/${fridgeId}`);
        expect(mockAxios.history.post[0].data).toEqual(JSON.stringify({
          groceryDTO: grocery,
          unitDTO: unit,
          quantity: quantity,
        }));
      });
    });
  
    describe('getGroceriesDTOs', () => {
      it('should get all grocery DTOs', async () => {
        const expectedResponse = {
          data: [{ name: 'Apple', quantity: 2 }]
        };
        mockAxios.onGet("/api/refrigerator/grocery/allDTOs").reply(200, expectedResponse.data);
  
        const response = await getGroceriesDTOs();
  
        expect(response.data).toEqual(expectedResponse.data);
        expect(mockAxios.history.get[0].url).toEqual("/api/refrigerator/grocery/allDTOs");
      });
    });
  
    describe('deleteGrocery', () => {
      it('should delete a grocery', async () => {
        const groceryEntity = {
            id : 1,
            physicalExpireDate : new Date(),
            refrigerator : {
                id : 1,
                name : "test",
                address : "test",
                members : null,
            },
            grocery: {
                id: 1,
                name: "test",
                description: "string",
                groceryExpiryDate: 1,
                subCategory: {
                    id : 1,
                    name :"test",
                    category : {
                        id : 1,
                        name : "test",
                    }
                },
            },
            unit : {id : 1, weight : 100, name : "dl"},
            quantity : 1,
        };
        const expectedResponse = {
          data: { message: 'Grocery deleted successfully' }
        };
        mockAxios.onDelete("/api/refrigerator/grocery/remove").reply(200, expectedResponse.data);
  
        const response = await deleteGrocery(groceryEntity);
  
        expect(response.data).toEqual(expectedResponse.data);
        expect(mockAxios.history.delete[0].url).toEqual("/api/refrigerator/grocery/remove");
      });
    });
});