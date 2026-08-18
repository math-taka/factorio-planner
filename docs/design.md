# Design

## Project Structure

- `factorio.model`
  - `ItemStack`
  - `Recipe`
  - `RecipeBook`
  - `FactoryType`
  - `ProductionSetting`
  - `ProductionNode`

- `factorio.calculator`
  - `RecipeCalculator`

- `factorio.io`
  - `RecipeLoader`


# factorio.model

## ItemStack

### Responsibility

- アイテム名と数量を保持する
- 同一アイテムかどうかを判定する
- 数量を加算した `ItemStack` を返す
- 数量を定数倍した `ItemStack` を返す

### Fields

- `item: String`
- `amount: double`

### Methods

- `sameItem(ItemStack other): boolean`
  - アイテム名が一致するかを判定する

- `addAmount(double value): ItemStack`
  - 数量を加算した `ItemStack` を生成する

- `multiplyAmount(double ratio): ItemStack`
  - 数量を定数倍した `ItemStack` を生成する

### Notes

- `record` で実装する
- Immutable
- ItemStack 自身に関する基本操作のみを持つ


## Recipe

### Responsibility

- レシピの材料、生成物、作成時間、生産設備の種類、生産性モジュールの使用可否を保持する

### Fields

- `ingredients: List<ItemStack>`
- `products: List<ItemStack>`
- `craftingTime: double`
- `factoryType: FactoryType`
- `usableProductivityModule: boolean`

### Methods

- 各種フィールドを参照するメソッド
  - `record` により自動的に実装される

### Notes

- `record` で実装する
- Immutable
- 計算機能は持たせない


## RecipeBook

### Responsibility

- 計算に使用するレシピの対応関係を保持する
- 指定されたアイテムのレシピが登録されているかを判定する
- 指定されたアイテムのレシピを取得する

### Fields

- `book: Map<String, Recipe>`

`Map.copyOf()` を使用して Immutable とする。

### Methods

- `getRecipe(String item): Recipe`
  - 指定されたアイテムを生産するためのレシピを返す

- `isContained(String item): boolean`
  - 指定されたアイテムについてレシピが登録されているかを判定する

### Constructors

- `RecipeBook(Map<String, Recipe> book)`
  - レシピの対応関係を直接受け取る

- `RecipeBook(List<Recipe> recipes)`
  - レシピの生成物からアイテムとレシピの対応関係を生成する

### Notes

- `class` で実装する
- 計算機能は持たせない
- レシピの対応関係のみを保持する
- `Map.copyOf()` により Immutable とする


## FactoryType

### Responsibility

生産に使用する設備の種類を表現する。

### Values

- `FURNACE`
- `ASSEMBLER`
- `CHEMICAL_PLANT`

### Notes

- `enum` で実装する


## ProductionSetting

### Responsibility

生産計算に使用する生産性ボーナスと生産速度を保持する。

### Fields

- `furnaceProductivity: double`
- `assemblerProductivity: double`
- `chemicalPlantProductivity: double`
- `furnaceProductionSpeed: double`
- `assemblerProductionSpeed: double`
- `chemicalPlantProductionSpeed: double`

### Notes

- `record` で実装する
- Immutable
- 計算機能は持たせない


## ProductionNode

### Responsibility

生産ツリー上の1つのノードを表現する。

### Fields

- `itemStack: ItemStack`
- `children: List<ProductionNode>`

### Notes

- `record` で実装する
- Immutable
- `children` は `List.copyOf()` により Immutable とする
- `children` は、そのアイテムを生産するために必要な材料の生産ノードを表す


# factorio.calculator

## RecipeCalculator

### Responsibility

`RecipeBook` に登録されたレシピに従って、目的のアイテムを生産するために必要な材料、生産ツリー、および必要な生産設備数を計算する。

### Methods

#### calculateIngredients

- `calculateIngredients(ItemStack target, RecipeBook recipeBook): List<ItemStack>`
  - 生産性ボーナスをすべて0として、指定されたアイテムに必要な材料を計算する

- `calculateIngredients(ItemStack target, RecipeBook recipeBook, ProductionSetting setting): List<ItemStack>`
  - 指定された生産設定を考慮して、必要な材料を計算する

- `calculateIngredients(List<ItemStack> targets, RecipeBook recipeBook): List<ItemStack>`
  - 複数のアイテムに必要な材料を計算し、同じアイテムを合算する

- `calculateIngredients(List<ItemStack> targets, RecipeBook recipeBook, ProductionSetting setting): List<ItemStack>`
  - 複数のアイテムについて、生産設定を考慮して必要な材料を計算する
  - 同じアイテムは合算する

#### calculateProductionTree

- `calculateProductionTree(ItemStack target, RecipeBook recipeBook, ProductionSetting setting): ProductionNode`
  - 指定されたアイテムの生産ツリーを計算する

- `calculateProductionTree(List<ItemStack> targets, RecipeBook recipeBook, ProductionSetting setting): List<ProductionNode>`
  - 複数のアイテムについて生産ツリーを計算する

#### calculateFacilityRequirements

- `calculateFacilityRequirements(ProductionNode node, RecipeBook recipeBook, ProductionSetting setting): Map<FactoryType, Double>`
  - 生産ツリーから必要な生産設備数を計算する

- `calculateFacilityRequirements(List<ProductionNode> nodes, RecipeBook recipeBook, ProductionSetting setting): Map<FactoryType, Double>`
  - 複数の生産ツリーについて必要な生産設備数を計算し、設備種別ごとに合算する

### Algorithm: Ingredient Calculation

1. 対象アイテムのレシピが存在しない場合、対象の `ItemStack` をそのまま返す
2. `RecipeBook` からレシピを取得する
3. 対象アイテムの必要生産倍率を計算する
4. 生産性モジュールが使用可能なレシピの場合、生産性ボーナスを考慮する
5. レシピの各材料について必要量を計算する
6. 材料について再帰的に `calculateIngredients()` を実行する
7. 同じアイテムを合算する
8. 必要材料のリストを返す

### Algorithm: Production Tree Calculation

1. 対象アイテムのレシピが存在しない場合、子ノードを持たない `ProductionNode` を生成する
2. レシピを取得する
3. 必要生産倍率を計算する
4. 各材料について必要量を計算する
5. 材料ごとに再帰的に `ProductionNode` を生成する
6. 対象アイテムと生成した子ノードから `ProductionNode` を生成する

### Algorithm: Facility Requirement Calculation

1. 生産ツリーのノードから対象レシピを取得する
2. 必要な生産回数を計算する
3. レシピの作成時間から基本生産速度を計算する
4. `FactoryType` に応じた生産速度を `ProductionSetting` から取得する
5. 必要生産回数と生産速度から必要設備数を計算する
6. 子ノードについて再帰的に必要設備数を計算する
7. 設備種別ごとに結果を合算する
8. 必要設備数を返す

### Notes

- `class` で実装する
- 計算ロジックを担当する
- `Recipe` や `RecipeBook` 自身には計算機能を持たせない
- 生産性に対応済み
- 生産速度に対応済み
- 複数ターゲットに対応済み
- 生産ツリーの計算に対応済み
- 生産設備数の計算に対応済み


# factorio.io

## RecipeLoader

### Responsibility

JSON ファイルから `RecipeBook` を生成する。

### Methods

- `load(File file): RecipeBook`
  - JSON ファイルを読み込み、`RecipeBook` を生成する

### Algorithm

1. JSON ファイルを読み込む
2. JSONを `List<Recipe>` に変換する
3. `RecipeBook` を生成する
4. 生成した `RecipeBook` を返す

### Notes

- `static` メソッドとして実装する
- Jackson (`ObjectMapper`) を利用する
- JSONの読み込みに失敗した場合は `IOException` を送る


# 設計上の基本方針

## Immutable Data Model

`factorio.model` に含まれるデータモデルは、可能な限り Immutable とする。

- `record` を利用する
- `List.copyOf()` を利用する
- `Map.copyOf()` を利用する

計算処理によって既存のオブジェクトを変更するのではなく、新しい値を生成する。


## Responsibility Separation

各クラスの責務を分離する。

- `ItemStack`
  - アイテムと数量を保持する

- `Recipe`
  - レシピ情報を保持する

- `RecipeBook`
  - 使用するレシピの対応関係を保持する

- `ProductionSetting`
  - 生産計算に使用する設定を保持する

- `ProductionNode`
  - 生産ツリーを表現する

- `RecipeLoader`
  - JSONからレシピ情報を読み込む

- `RecipeCalculator`
  - 生産に関する計算を行う

# 現在の構成

現在のシステムは、レシピデータを読み込み、`RecipeCalculator` を中心に
材料計算・生産ツリー計算・生産設備数計算を行う構成になっている。

## データの流れ

レシピデータは JSON ファイルから `RecipeLoader` によって読み込まれ、
`RecipeBook` として管理される。

```text
JSON
 │
 ▼
RecipeLoader
 │
 ▼
RecipeBook
 │
 ├── Recipe
 │    ├── ingredients
 │    ├── products
 │    ├── craftingTime
 │    ├── factoryType
 │    └── usableProductivityModule
 │
 ▼
RecipeCalculator
 │
 ├── 材料計算
 │      └── List<ItemStack>
 │
 ├── 生産ツリー計算
 │      └── ProductionNode
 │
 └── 生産設備数計算
        └── Map<FactoryType, Double>
```

`RecipeBook` は計算に使用するレシピを保持し、
`RecipeCalculator` がそれを利用して各種計算を行う。

`ProductionSetting` は Productivity や生産速度などの計算条件として
`RecipeCalculator` に渡される。

## クラスの関係

```text
RecipeBook
   │
   └── Recipe
        │
        └── FactoryType


RecipeCalculator
   ├── RecipeBook
   ├── ProductionSetting
   ├── ItemStack
   └── ProductionNode
                    │
                    └── ProductionNode
                         └── ...


RecipeLoader
   │
   └── RecipeBook
```

各クラスの役割は以下のように分かれている。

- `RecipeLoader`
  - JSONからレシピデータを読み込む
  - `RecipeBook` を生成する

- `RecipeBook`
  - 計算に使用する `Recipe` を管理する

- `Recipe`
  - 1つのレシピに必要な材料、生成物、作成時間、生産設備などを保持する

- `ProductionSetting`
  - Productivity と生産速度を保持する
  - `RecipeCalculator` の計算条件として使用される

- `RecipeCalculator`
  - `RecipeBook` と `ProductionSetting` を利用して計算を行う
  - 材料計算
  - 生産ツリー計算
  - 必要生産設備数計算

- `ProductionNode`
  - 生産ツリーの1つのノードを表現する
  - 子ノードとして、対象アイテムの生産に必要な材料の生産ツリーを保持する

このように、**データを保持するクラスと計算を行うクラスを分離する**ことを基本方針としている。


# 更新履歴

## Ver0.1

- レシピ計算機を実装
- 再帰計算に対応

## Ver0.2

- JUnit を導入
- `RecipeCalculator` をリファクタリング
- `ItemStack` に `multiplyAmount()` を追加
- 設計書を更新

## Ver0.3

- `calculateIngredients(List<ItemStack>, RecipeBook)` を追加
- 複数アイテムに対する材料の合算をサポート
- 複数アイテムに対する計算用のテストを追加
- 循環レシピの検出は未実装

## Ver0.4

- Jackson を導入
- `RecipeLoader` を追加
- JSON から `RecipeBook` を読み込む機能を追加
- JSON の読み込みテストを JUnit に追加

## Ver0.5

- デモアプリケーションを作成
- 計算機能を実際のアプリケーションから利用できる状態にした

## Ver0.6

- Productivity に対応
- 生産設備ごとの Productivity を設定できるようにした
- 生産速度を `ProductionSetting` で設定できるようにした

## Ver0.7

- `ProductionNode` を追加
- 生産ツリーを計算できるようにした
- 複数ターゲットの生産ツリーに対応

## Ver0.8

- 生産設備数の計算に対応
- `FactoryType` ごとの必要設備数を計算できるようにした
- 生産速度を考慮した設備数計算に対応

## Ver0.9

- JavaFX GUI の開発を開始
- UI設計を実施
- JavaFX のセットアップを実施
- 入力UI、Calculatorとの接続、計算結果表示を実装予定