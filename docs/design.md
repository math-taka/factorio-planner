# ItemStack

## Responsibility
アイテムと数量の組を表す。

## Fields
- item
- amount

## Methods
- sameItem(ItemStack other) return boolean
   item が一致するかを判定する

- addAmount(double value) reutrn ItemStack
    量を加算した ItemStack を生成する

## Notes
- recordで実装する
- Immutable
- 計算処理は持たせない×
　→量の足し算ができる

# Recipe

## Responsibility
レシピの材料、生成物、作成条件を表す

## Field
- ingredients:List<ItemStack>
- products:List<ItemStack>
- craftingTime:double

今は実装しないがあとで必要になるかもしれないもの
- factoryType 生産設備の種類
- usableProductivityModule:boolean(今は実装しない)

## Methods
- 各種フィールドを参照するメソッド(record で自動的に実装される)

## Notes
- Immutable
- 計算等の機能は持たせない
- Record で実装する

# RecipeBook

## Responsibility 
計算に使用するレシピ選択を保持する

## Fields
 - book:Map<String,Recipe> 
 
## Methods 
 - getRecipe(String) return Recipe 
 　　アイテムを生産するためのレシピを返す 

## Notes 
 - Class で実装する 
 - 計算はしない
 - 対応関係のみを保持する
 ‐ Ver0.1 では変更不可(Immutable)

# RecipeCalculator

## Responsibility

 RecipeBookに登録されたレシピに従って目的のアイテムを生成するための材料を計算する

## Fields

## Methods

 - calculateIngredients(ItemStack target,RecipeBook recipeBook) return List<ItemStack>
    ItemStack で指定されたアイテムと数量分を RecipeBook のレシピ通りに生産するために必要な素材を計算する。
    戻り値のリストには同じアイテム名の ItemStack が重複しないようにする。

## Algorithm
目的のアイテムとその個数を受け取る
↓
レシピを取得
　├　レシピがない→そのまま返す
　└　レシピがある→材料を計算
↓
各材料について材料を計算しリストに加える
↓
重複した材料を合算

## Notes
 - class で実装
 - 計算機能のみを持つ

## Future
 - 将来的には複数種類の生産物に対しての計算の実装も考える
