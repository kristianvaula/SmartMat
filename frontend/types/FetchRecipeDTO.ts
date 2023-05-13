import {integer} from "vscode-languageserver-types";

export interface FetchRecipeDTO {
    refrigeratorId: integer,
    numRecipes: integer,
    recipesFetched: Array<integer>,
}