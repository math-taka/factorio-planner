# ItemStack

## Responsibility
アイテムと数量の組を表す。

## Fields
- item
- amount

## Methods
- なし

## Notes
- recordで実装する
- Immutable
- 計算処理は持たせない

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