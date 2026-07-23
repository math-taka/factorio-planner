factorio.model

# ItemStack

## Responsibility
- アイテム名と数量を保持する
- 同一アイテムかどうかを判定する
- 数量を加算した ItemStack を返す
- 数量を定数倍した ItemStack を返す

## Fields
- item
- amount

## Methods
- sameItem(ItemStack other) return boolean
   item が一致するかを判定する

- addAmount(double value) reutrn ItemStack
    数量を加算した ItemStack を生成する

- multiplyAmount(double ratio) return ItemStack
    数量を定数倍した ItemStack を生成する

## Notes
- recordで実装する
- Immutable
- ItemStack 自身に関する基本操作は実装する

# Recipe

## Responsibility
- レシピの材料、生成物、作成時間を保持する

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
- 計算に使用するレシピ選択を保持する
- 指定のアイテムのレシピが選択されたレシピの中に存在するかを判定する

## Fields
 - book:Map<String,Recipe>
    Map.copyOf を使用しており Immutable
 
## Methods 
 - getRecipe(String) return Recipe 
 　　アイテムを生産するためのレシピを返す 

 - isContained(String item) return boolean
    そのアイテムについてのレシピがあるかを判定する

## Notes 
 - Class で実装する 
 - 計算はしない
 - 対応関係のみを保持する
 ‐ Ver0.1 では変更不可(Immutable)

factorio.calculator

# RecipeCalculator

## Responsibility

 RecipeBookに登録されたレシピに従って目的のアイテムを生成するための材料を計算する

## Fields

## Methods

 - calculateIngredients(ItemStack target,RecipeBook recipeBook) return List<ItemStack>
    ItemStack で指定されたアイテムと数量分を RecipeBook のレシピ通りに生産するために必要な素材を計算する。
    戻り値のリストには同じアイテム名の ItemStack が重複しないようにする。
 - calculateIngredients(List<ItemStack> targets,RecipeBook recipeBook) return List<ItemStack>
    複数アイテムの必要素材を計算し、同じ素材は合算した結果を返す

## Algorithm
    1. レシピが存在しないなら target を返す
    2. レシピを取得
    3. 生産倍率を計算
    4. 各材料について
        ・倍率を掛けて必要数を計算
        ・再帰的に材料を計算
        ・結果を合算
    5. 結果を返す

## Notes
 - class で実装
 - 計算機能のみを持つ

## Future
 - 生産ツリーを返す計算への拡張
 - 生産設備を考慮した計算
 - モジュールへの対応
 - 循環レシピの検出


factorio.io

# RecipeLoader
## Responsibility

 - JSON ファイルから RecipeBook を生成する

## Methods

 - load(File file) return RecipeBook
    JSON ファイルを読み込み、RecipeBook を生成する

## Algorithm
 1. JSON ファイルを読み込む
 2. List<Recipe> に変換
 3. RecipeBook を生成
 4. 生成した RecipeBook を返す

## Notes
 - static メソッドとして実装
 - Jackson(ObjectMapper) を利用
 - JSONの読み込みに失敗した場合は IOException を送る

#更新履歴

## Ver0.1
- レシピ計算機を実装
- 再帰計算に対応

## Ver0.2
- JUnit を導入
- RecipeCalculator をリファクタリング
- ItemStack に multiplyAmount() を追加
- 設計書を更新

## Ver0.3
- calculateIngredients(List,RecipeBook) を追加して複数アイテムに対する材料の合算をサポート
- 複数アイテムに対する計算用のテストを追加

## Ver0.4
 - Jackson を導入
 - RecipeLoader を追加
 - JSON から RecipeBook を読み込む機能を追加
 - JSON の読み込みテストを JUnit に追加